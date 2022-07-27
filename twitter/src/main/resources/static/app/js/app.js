let app = angular.module('tweetApp', ['ngRoute', 'ngResource', 'ngFileUpload']);

app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$locationProvider.html5Mode({ enabled: true });

	$routeProvider
		.when('/login', {
			templateUrl: '/app/template/account/login.html',
			controller: 'loginCtrl'
		})
		.when('/signup', {
			templateUrl: '/app/template/account/signup.html',
			controller: 'signupCtrl'
		})
		.when('/home', {
			templateUrl: '/app/template/home/home.html',
			controller: 'homeCtrl'
		})
		.when('/profile/:id', {
			templateUrl: '/app/template/profile/profile.html',
			controller: 'profileCtrl'
		})
		.when('/tweet/:id', {
			templateUrl: '/app/template/tweet/tweet.html',
			controller: 'tweetCtrl'
		})
		.when('/relation/:id/:type', {
			templateUrl: '/aPP/template/profile/relation.html',
			controller: 'relationCtrl'
		})
		.when('/', {
			templateUrl: '/app/template/home/home.html',
			controller: 'homeCtrl'
		})
		.otherwise({
			redirectTo: '/home'
		})

}]);

app.factory('RegisterApi', function($resource) {

	let base = '/register';
	return $resource(base, {}, {
		login: {
			method: 'POST',
			url: base + '/login'
		},
		signup: {
			method: 'POST',
			url: base + '/signup'
		},
		logout: {
			method: 'POST',
			url: '/logout'
		}
	})
});

app.factory('AccountApi', function($resource) {

	let base = '/accounts';
	return $resource(base, {}, {
		search: {
			method: 'GET',
			url: base + '/search',
			isArray: true
		},
		current: {
			method: 'GET',
			url: base + '/principal'
		},
		getSomeone: {
			method: 'GET',
			url: base + '/:id'
		},
		follow: {
			method: 'Get',
			url: base + '/follow/:id'
		},
		unfollow: {
			method: 'GET',
			url: base + '/unfollow/:id'
		},
		getFollows: {
			method: 'GET',
			url: base + '/:id/follows',
			isArray: true
		},
		getFollowers: {
			method: 'GET',
			url: base + '/:id/followers',
			isArray: true
		}
	})
});

app.factory('RelationApi', function($resource) {

	let base = '/relations';
	return $resource(base, {}, {
		follow: {
			method: 'Get',
			url: base + '/follow/:id'
		},
		unfollow: {
			method: 'GET',
			url: base + '/unfollow/:id'
		},
		getFollows: {
			method: 'GET',
			url: base + '/:id/follows',
			isArray: true
		},
		getFollowers: {
			method: 'GET',
			url: base + '/:id/followers',
			isArray: true
		}
	})
});

app.factory('TweetApi', function($resource) {

	let base = '/tweets';
	return $resource(base, {}, {
		list: {
			method: 'GET',
			url: base + '/list/:id',
			isArray: true
		},
		get: {
			method: 'GET',
			url: base + '/tweet/:id'
		},
		getComments: {
			method: 'GET',
			url: base + '/:id/comments',
			isArray: true
		},
		tweet: {
			method: 'POST',
			url: base + '/tweet'
		},
		quote: {
			method: 'POST',
			url: base + '/quote'
		},
		comment: {
			method: 'POST',
			url: base + '/comment'
		},
		like: {
			method: 'GET',
			url: base + '/like/:id'
		},
		retweet: {
			method: 'GET',
			url: base + '/retweet/:id'
		},
		delete: {
			method: 'DELETE',
			url: base + '/:id'
		},
		uploadImage: {
			method: 'POST',
			url: base + '/image'
		}
	})
});

app.service('AccountService', function() {

	let observers = [];

	this.subscribe = function(f) {
		observers.push(f);
	};

	this.setAccount = function(a) {
		account = a;
		observers.forEach(f => f(account));
	};

	this.getAccount = function() {
		if (account != null) {
			return account;
		}
	};
});

app.service('TweetService', function() {
	let observers = [];

	this.subscribe = function(func) {
		observers.push(func);
	};

	this.setTweet = function(twt) {
		observers.forEach(f => f(twt));
	};

	this.removeTweet = function(twt) {
		observers.forEach(f => f(twt, true));
	}
});

app.service('ImageService', function(Upload) {

	this.save = function(image, tweetDto, callback) {
		Upload.upload({
			url: '/tweets/' + tweetDto.tweet.id + '/image',
			method: 'POST',
			data: { file: image }
		}).then(function onSuccess(response) {
			tweetDto.tweet.image = response.data;
			callback(tweetDto);
		}).catch(function onError(response) {
			console.log(response);
		});
	}
});

app.service('CommentService', function(TweetApi) {

	this.comment = function(item, callback) {
		TweetApi.comment(item, function(resp) {
			item.tweet.target.commentNo += 1;
			callback(resp);
		});
	};
});

app.service('QuoteService', function(TweetApi) {

	this.quote = function(item, callback) {
		TweetApi.quote(item, function(resp) {
			item.retweetNo += 1;
			callback(resp);
		});
	};
});

app.service('RetweetService', function(TweetApi, TweetService) {

	this.retweet = function(id, item, commentpage) {
		TweetApi.retweet({ id: id }, function(resp) {
			if (resp.code == 10) {
				if (commentpage == true) {
					toastr.success('Retweet posted');
				} else {
					TweetService.setTweet(resp);
				}
				item.retweeted = true;
				item.tweet.retweetNo += 1;
			} else {
				if (commentpage == true) {
					toastr.success('Retweet deleted');
				} else {
					TweetService.removeTweet(resp);
				}
				item.retweeted = false;
				item.tweet.retweetNo -= 1;
			}
		});
	};
});

app.service('LikeService', function(TweetApi) {

	this.like = function(id, item) {
		TweetApi.like({ id: id }, function(resp) {
			if (resp.code == 5) {
				if (item.tweet.type == 'RETWEET') {
					item.tweet.target.likeNo -= 1;
				} else {
					item.tweet.likeNo -= 1;
				}
				item.liked = false;
			} else {
				if (item.tweet.type == 'RETWEET') {
					item.tweet.target.likeNo += 1;
				} else {
					item.tweet.likeNo += 1;
				}
				item.liked = true;
			}
		});
	};
});

app.service('RelationService', function(RelationApi) {

	this.follow = function(item) {
		RelationApi.follow({ id: item.id }, function(resp) {
			if (resp.id != 0) {
				toastr.success(item.name + ' is following now');
			}
		});
	};

	this.unfollow = function(item, type, list) {
		RelationApi.unfollow({ id: item.id, type: type });
		if (list) {
			_.remove(list, function(i) {
				if (type == 'following') {
					if (i.followed.account.id == item.id) {
						return i;
					}
				} else {
					if (i.follower.account.id == item.id) {
						return i;
					}
				}
			});
		}
	};
});


app.controller('HeaderCtrl', function($scope, $location, RegisterApi,
	AccountApi, RelationService, AccountService) {

	$scope.logout = function() {
		RegisterApi.logout(function() {
			AccountService.setAccount(null);
			$location.path('/login');
		});
	};

	$scope.goToProfile = function(accnt) {
		if (accnt.id == $scope.account.id) {
			$location.path('/profile/me');
		} else {
			$location.path('/profile/' + accnt.id);
		}
	};

	$scope.unfollow = function(accnt) {
		RelationService.unfollow(accnt, "following");
		$scope.list = [];
		$scope.name = '';
	};


	$scope.follow = function(accnt) {
		RelationService.follow(accnt);
		$scope.list = [];
		$scope.name = '';
	};

	$scope.search = function() {
		if ($scope.name && $scope.name.length === 3) {
			$scope.list = AccountApi.search({ name: $scope.name });
		}
		if ($scope.name == '' || $scope.name.length < 3) {
			$scope.list = [];
		}
	};

	$scope.getAccount = function() {
		AccountApi.current(function(resp) {
			if (resp.id) {
				AccountService.setAccount(resp);
			}
		});
	};

	$scope.checkAccount = function(a) {
		if (a != null && a.id) {
			$scope.account = a;
			$scope.tweet.account = a;
			$scope.visible = true;
		} else {
			$scope.visible = false;
		}
	};

	$scope.init = function() {
		$scope.name = '';
		$scope.tweet = { account: {} };
		$scope.getAccount();
		AccountService.subscribe($scope.checkAccount);
	};
	$scope.init();

});
