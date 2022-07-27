angular.module('tweetApp')
	.controller('loginCtrl', function($scope) {

		$scope.action = 'Login';
		$scope.message = 'Invaid username or password!';
		$scope.path = 'Signup';
		$scope.content = 'Dont have an account?';

	}).directive('myForm', function($location, $window, RegisterApi) {

		return {
			restrict: 'E',
			templateUrl: '/app/template/account/my-form.html',
			trasnclude: true,
			scope: {
				action: '=',
				path: '=',
				loginrequest: '=',
				message: '=',
				content: '=',
				submit: '&'
			},
			link: function(scope) {
				scope.goto = function() {
					if (scope.path == 'Signup') {
						$location.path('/signup');
					} else {
						$location.path('/login');
					}
				};

				scope.submit = function() {
					if (scope.action == 'Login') {
						RegisterApi.login(scope.loginrequest, function(resp) {
							if (resp.code == 10) {
								$window.location.href = "/home";
							} else {
								scope.error = true;
							}
						});
					}
					if (scope.action == 'Register') {
						RegisterApi.signup(scope.loginrequest, function(resp) {
							if (resp.code == 10) {
								$location.path('/login');
							} else {
								scope.error = true;
							}
						});
					}
				};
			}
		}
	});