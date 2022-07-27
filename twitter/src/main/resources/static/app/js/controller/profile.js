angular.module('tweetApp')
	.controller('profileCtrl', function($scope, $location, $routeParams, AccountApi,
		AccountService, RelationService, TweetService, TweetApi, Upload) {

		$scope.follow = function() {
			RelationService.follow($scope.profile);
			$scope.isFollow = true;
		};

		$scope.unfollow = function() {
			RelationService.unfollow($scope.profile, "following");
			$scope.isFollow = false;
		};

		$scope.savePhoto = function() {
			Upload.upload({
				url: '/accounts/image',
				method: 'PUT',
				data: { file: $scope.image },
				params: { defaultImage: $scope.defaultImage }
			}).then(function onSuccess(response) {
				if (response.data) {
					AccountService.setAccount(response.data);
					$('#profileModal').modal('hide');
				}
			}).catch(function onError(response) {
				console.log(response);
				toastr.warning('There is an error');
				$('#profileModal').modal('hide');
			});
		};

		$scope.deletePhoto = function() {
			$scope.newSrc = '/app/img/profile.png';
			$scope.defaultImage = 'default';
			$scope.image = null;
		};

		$scope.onloadPhoto = function(file) {
			if (file) {
				$scope.newSrc = URL.createObjectURL(file);
				$scope.image = file;
				$scope.defaultImage = '';
			}
		};

		$scope.cancel = function() {
			$('#profileModal').modal('hide');
			$scope.newSrc = null;
		}

		$scope.deleteTweet = function(item) {
			TweetApi.delete({ id: item.tweet.id });
			_.remove($scope.list, item);
		};

		$scope.loadModal = function(item) {
			item.tweet.account = $scope.profile;
			if (item.tweet.type == 'RETWEET') {
				$scope.item.tweet.target = item.tweet.target;
			} else {
				$scope.item.tweet.target = item.tweet;
			}
		};

		$scope.commentModal = function(item) {
			$scope.loadModal(item);
			$scope.type = "COMMENT";
			$('#commentModal').modal('show');
		};


		$scope.quoteModal = function(item) {
			$scope.loadModal(item);
			$scope.type = "QUOTE";
			$('#quoteModal').modal('show');
		};

		$scope.goToRelation = function(type) {
			if ($scope.id == 'me') {
				$location.path('/relation/me/' + type);
			} else {
				$location.path('/relation/' + $scope.profile.id + '/' + type);
			}
		};

		$scope.loadPage = function() {
			if ($scope.initLoad == false) {
				$scope.current += 1;
				$scope.getTweets();
			}
			$scope.initLoad = false;
		};

		$scope.getTweets = function() {
			TweetApi.list({ id: $scope.id, type: 'profile', current: $scope.current, size: $scope.size }, function(resp) {
				if (resp != null) {
					resp.forEach(i => {
						if (!$scope.list.includes(i)) {
							$scope.list.push(i);
						}
					});
				}
			});

		};

		$scope.checkTweet = function(t, remove) {
			if (t != null && t.tweet) {
				if ($scope.id == 'me') {
					if (remove) {
						_.remove($scope.list, function(n) {
							return n.tweet.id == t.tweet.id;
						});
					} else {
						$scope.list.push(t);
					}
				} else {
					toastr.success('Tweet posted');
				}
			}
		};

		$scope.checkAccount = function(a) {
			if (a != null && a.id) {
				$scope.item.tweet.account = a;
				$scope.account = a;
				if ($scope.id == 'me') {
					$scope.profile = a;
				}
				if ($scope.list) {
					$scope.list.forEach(i => {
						if (i.tweet.account.id == a.id) {
							i.tweet.account = a;
						}
						if (i.tweet.target != null && i.tweet.target.account.id == a.id) {
							i.tweet.target.account = a;
						}
					});
				}
			}
		};

		$scope.init = function() {
			$scope.id = $routeParams.id;
			$scope.current = 0;
			$scope.size = 8;
			$scope.initLoad = true;
			$scope.list = [];
			$scope.account = AccountService.getAccount();
			$scope.item = { tweet: { account: {}, target: {} } };
			$scope.item.tweet.account = $scope.account;
			if ($scope.id == 'me') {
				$scope.profile = $scope.account;
				$scope.me = true;
				$scope.getTweets();
			} else {
				AccountApi.getSomeone({ id: $scope.id }, function(resp) {
					if (resp.account) {
						$scope.profile = resp.account;
						if (resp.follow) {
							$scope.isFollow = true;
						}
						$scope.getTweets();
					} else {
						$location.path('/home');
					}
				});
				$scope.me = false;
			}
			AccountService.subscribe($scope.checkAccount);
			TweetService.subscribe($scope.checkTweet);
		};
		$scope.init();

	});