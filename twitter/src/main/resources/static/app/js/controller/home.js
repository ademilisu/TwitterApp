angular.module('tweetApp')
	.controller('homeCtrl', function($scope, AccountService, TweetService, TweetApi) {
		$scope.current = 0;
		$scope.size = 8;

		$scope.loadPage = function() {
			$scope.current += 1;
			$scope.getList();
		};

		$scope.delete = function(item) {
			if ($scope.account.id == item.tweet.account.id) {
				TweetApi.delete({ id: item.tweet.id });
				_.remove($scope.list, item);
			}
		};

		$scope.loadModal = function(item) {
			$scope.item = { tweet: { account: {}, target: {} } };
			$scope.item.tweet.account = $scope.account;
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

		$scope.checkTweet = function(t, remove) {
			if (t != null && t.tweet) {
				if (remove) {
					_.remove($scope.list, function(n) {
						return n.tweet.id == t.tweet.id;
					});
				} else {
					$scope.list.push(t);
				}
			}
		};

		$scope.checkAccount = function(a) {
			if (a != null && a.id) {
				$scope.newTweet.account = a;
				$scope.account = a;
			}
		};

		$scope.getList = function() {
			TweetApi.list({ id: 'm', type: 'home', current: $scope.current, size: $scope.size }, function(resp) {
				if (resp != null) {
					resp.forEach(i => {
						$scope.list.push(i);
					});
				}
			});
		};

		$scope.init = function() {
			$scope.list = [];
			$scope.newTweet = { account: {} };
			$scope.newTweet.account = AccountService.getAccount();
			$scope.account = AccountService.getAccount();
			$scope.getList();
			AccountService.subscribe($scope.checkAccount);
			TweetService.subscribe($scope.checkTweet);
		};
		$scope.init();
	});