angular.module('tweetApp')
	.directive('replyContent', function(TweetService, CommentService, QuoteService, ImageService) {

		return {
			restrict: 'E',
			templateUrl: '/app/template/directives/reply-content.html',
			transclude: true,
			scope: {
				item: '=',
				type: '=',
				commentpage: '=',
				hidetarget: '='
			},
			link: function(scope) {

				scope.dismiss = function() {
					scope.loadedFile = null;
					scope.item.tweet.content = null;
					scope.limit = 0;
					$('#commentModal').modal('hide');
					$('#quoteModal').modal('hide');
				};

				scope.send = function() {
					let tweetDto = { tweet: {} };
					tweetDto.tweet = scope.item.tweet;
					if (scope.type == 'COMMENT') {
						CommentService.comment(tweetDto, scope.tweetCallback);
					}
					if (scope.type == 'QUOTE') {
						QuoteService.quote(tweetDto, scope.tweetCallback);
					}
				};

				scope.tweetCallback = function(tweetDto) {
					if (scope.loadedFile) {
						ImageService.save(scope.loadedFile, tweetDto, scope.finishCallback);
					} else {
						scope.finishCallback(tweetDto);
					}
				};

				scope.finishCallback = function(tweetDto) {
					if (scope.commentpage == true) {
						toastr.success('Tweet posted.');
					} else {
						TweetService.setTweet(tweetDto);
					}
					scope.dismiss();
				}

				scope.selectImage = function(file) {
					scope.loadedFile = file;
					scope.disable = false;
				};

				scope.init = function() {
					scope.typeMessage = 'Tweet your reply';
					scope.disable = true;
				};
				scope.init();
			}
		}
	});