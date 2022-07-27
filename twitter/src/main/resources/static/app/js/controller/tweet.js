angular.module('tweetApp')
	.controller('tweetCtrl', function($scope, $location, $routeParams,
		TweetApi, AccountService, TweetService, LikeService, RetweetService) {

		$scope.like = function(item) {
			LikeService.like(item.tweet.id, item);
		};

		$scope.retweet = function(item) {
			RetweetService.retweet(item.tweet.id, item, $scope.type);
		};

		$scope.loadModal = function(item) {
			$scope.new = { tweet: { account: {}, target: {} } };
			$scope.new.tweet.account = $scope.account;
			$scope.new.tweet.target = item.tweet;
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

		$scope.checkTweet = function(t, remove) {
			if (t != null && t.tweet) {
				if (remove) {
					_.remove($scope.list, function(n) {
						return n.tweet.id == t.tweet.id;
					});
				}
				else $scope.list.push(t);
			}
		};

		$scope.checkAccount = function(a) {
			if (a != null && a.id) {
				$scope.newReply.tweet.account = a;
				$scope.account = a;
			}
		};

		$scope.init = function() {
			$scope.id = $routeParams.id;
			TweetApi.get({ id: $scope.id }, function(resp) {
				if (resp.code == 10) {
					$scope.item = resp;
					$scope.newReply.tweet.target = resp.tweet;
				} else {
					$location.path('/home');
				}
			});
			$scope.list = TweetApi.getComments({ id: $scope.id });
			$scope.newReply = { tweet: { account: {}, target: {} } };
			$scope.account = AccountService.getAccount();
			$scope.newReply.tweet.account = $scope.account;
			$scope.newReply.tweet.target = $scope.item;
			$scope.type = 'COMMENT';
			AccountService.subscribe($scope.checkAccount);
			TweetService.subscribe($scope.checkTweet);
		}
		$scope.init();
	});