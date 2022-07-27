angular.module('tweetApp')
	.directive('messageTemplate', function($location, AccountService) {

		return {
			restrict: 'E',
			templateUrl: '/app/template/directives/message-template.html',
			transclude: true,
			scope: {
				tweet: '=',
				file: '=',
				limit: '=',
				typemessage: '=',
				profileimagesize: '=',
				resize: '=',
				disabletext: '=',
				hidedismiss: '=',
				disable: '=',
				cancel: '&',
			},
			link: function(scope) {
				let run = true;
				let hasImage = false;

				scope.close = function() {
					scope.deletePhoto();
					scope.cancel();
				};

				scope.deletePhoto = function() {
					if (scope.tweet.content) {
						scope.disable = false;
					} else {
						scope.disable = true;
					}
					scope.newsrc = '';
					scope.file = null;
					hasImage = false;
				};

				scope.onLoad = function() {
					if (hasImage == false && scope.file) {
						hasImage = true;
						scope.newSrc = URL.createObjectURL(scope.file);
					}
				};

				scope.calculate = function(key) {
					if (key.which == 8) {
						run = true;
					}
					if (run) {
						if (key.which == 8) {
							if (scope.limit > 0) {
								scope.limit -= 1;
							}
							if (scope.tweet.content == '') {
								scope.limit = 0;
							}
						}
						if (key.which != 8) {
							scope.limit += 1;
						}
					}
					if (scope.limit == 140) {
						scope.tweet.content = scope.tweet.content.substring(0, 140);
						run = false;
					}
					if (scope.limit > 0) {
						scope.disable = false;
					}
					if (scope.limit == 0) {
						scope.disable = true;
					}
				};

				scope.resizeTextArea = function() {
					if (scope.resize) {
						if (scope.tweet && scope.tweet.content) {
							let contentLength = scope.tweet.content.length;
							scope.row = Math.round(contentLength / (400 / 100 * 10));
						}
					}
				};

				scope.goToProfile = function() {
					let accnt = AccountService.getAccount();
					if (scope.tweet.account.id === accnt.id) {
						$location.path('/profile/me');
					} else {
						$location.path('/profile/' + scope.tweet.account.id);
					}
				};

				scope.init = function() {
					scope.newsrc = '';
					scope.row = 2;
					scope.resizeTextArea();
					if (scope.disabletext != true) {
						scope.limit = 0;
					}
				};
				scope.init();
			}
		}
	});