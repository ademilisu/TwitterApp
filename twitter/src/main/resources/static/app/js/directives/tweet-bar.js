angular.module('tweetApp')
	.directive('tweetBar', function() {
		return {
			restrict: 'E',
			templateUrl: '/app/template/directives/tweet-bar.html',
			transclude: true,
			scope: {
				tweet: '=',
				liked: '=',
				retweeted: '=',
				like: '&',
				quote: '&',
				comment: '&',
				retweet: '&'
			}
		}
	});
