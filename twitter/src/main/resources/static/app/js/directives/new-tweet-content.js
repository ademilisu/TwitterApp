angular.module('tweetApp')
	.directive('newTweetContent', function(ImageService, TweetApi, TweetService) {

		return {
			restrict: 'E',
			templateUrl: '/app/template/directives/new-tweet-content.html',
			transclude: true,
			scope: {
				tweet: '=',
			},
			link: function(scope) {

				scope.dismiss = function() {
					scope.tweet.content = null;
					scope.loadedFile = null;
					scope.limit = 0;
					$('#tweetModal').modal('hide');
				};
				
				scope.tweetCallback = function(tweetDto){
					TweetService.setTweet(tweetDto);
					scope.dismiss();
				}

				scope.send = function() {
					let tweetDto = { tweet: {}};
					tweetDto.tweet = scope.tweet;
					TweetApi.tweet(tweetDto, function(resp) {
						if (resp.tweet) {
							tweetDto = resp;
							if(scope.loadedFile) {
								ImageService.save(scope.loadedFile, tweetDto, scope.tweetCallback);
								scope.loadedFile = null;
							} else {
								scope.tweetCallback(tweetDto);
							}
						}
					});
				};

				scope.selectImage = function(f) {
					scope.loadedFile = f;
					scope.disable = false;
				};

				scope.init = function() {
					scope.disable = true;
					scope.typemessage = 'What is happening?';
				};
				scope.init();

			}
		}
	});