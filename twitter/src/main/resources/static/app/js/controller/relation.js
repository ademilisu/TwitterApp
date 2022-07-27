angular.module('tweetApp')
	.controller('relationCtrl', function($scope, $location, $routeParams, AccountApi,
		RelationApi, AccountService, RelationService) {

		$scope.id = $routeParams.id;
		$scope.section = $routeParams.type;

		$scope.back = function() {
			if ($scope.id == 'me') {
				$location.path('/profile/me');
			} else {
				$location.path('/profile/' + $scope.id);
			}
		};

		$scope.follow = function(item) {
			RelationService.follow(item.account);
		};

		$scope.unfollow = function(item) {
			let list = [];
			if ($scope.type == 'followers') {
				list = $scope.followers;
			} else {
				list = $scope.following;
			}
			RelationService.unfollow(item, $scope.type, list);

		};

		$scope.showFollowers = function() {
			$scope.type = 'followers';
			$scope.followersSection = true;
			$scope.followingSection = false;
			$scope.list = $scope.followers;
		};

		$scope.showFollowing = function() {
			$scope.type = 'following';
			$scope.followersSection = false;
			$scope.followingSection = true;
			$scope.list = $scope.following;
		};

		$scope.goToProfile = function(accnt) {
			if (accnt.id == $scope.account.id) {
				$location.path('/profile/me');
			} else {
				$location.path('/profile/' + accnt.id);
			}
		};

		$scope.accountCheck = function(a) {
			if (a != null && a.id) {
				if ($scope.id == 'me') {
					$scope.profile = a;
				}
				$scope.account = a;
			}
		};

		$scope.init = function() {
			if ($scope.id == 'me') {
				$scope.profile = AccountService.getAccount();
			} else {
				AccountApi.getSomeone({ id: $scope.id }, function(resp) {
					$scope.profile = resp.account;
				});
			}
			RelationApi.getFollows({ id: $scope.id }, function(resp) {
				$scope.following = resp;
				if ($scope.section == 'following') {
					$scope.showFollowing();
				}
			});
			RelationApi.getFollowers({ id: $scope.id }, function(resp) {
				$scope.followers = resp;
				if ($scope.section == 'followers') {
					$scope.showFollowers();
				}
			});
			$scope.account = AccountService.getAccount();
			AccountService.subscribe($scope.accountCheck);
		};
		$scope.init();

	}).filter('relationFilter', function() {

		return function(item, type) {
			let result = [];
			if (item) {
				item.forEach(a => {
					if (type == 'following') {
						result.push(a.followed);
					} else {
						result.push(a.follower)
					}
				});
			}
			return result;
		}
	});