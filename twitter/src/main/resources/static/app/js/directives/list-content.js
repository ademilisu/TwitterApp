angular.module('tweetApp')
	.directive('listContent', function($location, LikeService, RetweetService) {

		return {
			restrict: 'E',
			templateUrl: '/app/template/directives/list-content.html',
			transclude: true,
			scope: {
				item: '=',
				commentpage: '=',
				quote: '&',
				comment: '&',
				delete: '&',
			},
			link: function(scope) {

				let id = scope.item.tweet.id;
				if (scope.item.tweet.type == 'RETWEET') {
					id = scope.item.tweet.target.id;
				}

				scope.like = function() {
					LikeService.like(id, scope.item);
				};

				scope.retweet = function() {
					RetweetService.retweet(id, scope.item, scope.commentpage);
				};

				scope.goToTweet = function(tweet) {
					if (tweet) {
						$location.path('/tweet/' + tweet.id);
					} else {
						$location.path('/tweet/' + id);
					}
				};

				scope.init = function() {
					if (scope.item && scope.item.tweet.account.id != account.id) {
						scope.hidecancel = 'hide';
					}
				};
				scope.init();
			}
		}
	});