angular.module('tweetApp')
	.controller('signupCtrl', function($scope) {

		$scope.action = 'Register';
		$scope.message = 'Username has already taken!';
		$scope.path = 'Login';
		$scope.content = 'Have an account?';
	});