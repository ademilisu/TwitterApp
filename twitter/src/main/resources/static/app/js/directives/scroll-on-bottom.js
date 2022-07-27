angular.module('tweetApp')
	.directive('scrollOnBottom', function($document){
		
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				let x = angular.element($document)[0].body;
				
				$document.bind("scroll", function(){

					if((x.clientHeight + x.scrollTop + 20) >= x.scrollHeight){
						scope.$apply(attrs.scrollOnBottom);
					}
				});
				
			}
		}
	});