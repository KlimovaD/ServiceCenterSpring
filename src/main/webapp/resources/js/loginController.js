function InfoShareService() {
    var user = {};
    var receipt = {};
    return {
        setReceipt: function (value) {
            receipt = receipt;
        },
        getReceipt: function () {
            return receipt;
        },
        setUser: function (value) {
            user = value;
        },
        getUser: function () {
            return user;
        }
    };
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function LoginController($scope, $http, UserService, InfoShareService) {
    this.isRegister = false;
    this.signIn = function () {
        if (!$scope.login) {
            alert("Enter login");
        } else if (!$scope.passwd) {
            alert("Enter password");
        } else {
            $http.get('rest/user/' + $scope.login + '/authenticate?passwd=' + $scope.passwd)
                .then(function (response) {
                    if (response.data.toString() === "true") {
                        UserService.get({login:$scope.login}, function (data) {
                            InfoShareService.setUser(data);
                            window.location.href = '#/user/' + $scope.login;
                        });
                    } else {
                        alert("Incorrect login or password");
                        $scope.login = "";
                        $scope.passwd = "";
                    }
                }, function (error) {
                    alert("Incorrect login or password");
                    $scope.login = "";
                    $scope.passwd = "";
                });
        }
    };

    this.registerUser = function () {
        if (isEmpty($scope.loginReg)) {
            console.log($scope.loginReg);
            alert("Enter the login");
        } else if (isEmpty($scope.name)) {
            alert("Enter the name");
        } else if (isEmpty($scope.password1)) {
            alert("Enter the password");
        } else if (isEmpty($scope.surname)) {
            alert("Enter the surname");
        } else if (isEmpty($scope.phone)) {
            alert("Enter the phone number");
        } else if (isEmpty($scope.email)) {
            alert("Enter the e-mail");
        } else if ($scope.password1 !== $scope.password2) {
            alert("Passwords should be equal");
        } else {
            var user = new UserService();
            user.login = $scope.loginReg;
            user.name = $scope.name;
            user.surname = $scope.surname;
            user.patronymic = $scope.patronymic;
            user.phone = $scope.phone;
            user.role = $scope.role;
            user.email = $scope.email;
            user.password = $scope.password1;
            user.$save({login:$scope.loginReg}, function () {
                $scope.loginReg = "";
                $scope.name = "";
                $scope.surname = "";
                $scope.patronymic = "";
                $scope.phone = "";
                $scope.email = "";
                $scope.role = "";
                $scope.password1 = "";
                $scope.password2 = "";
                this.isRegister = false;
            }.bind(this), function (error) {
                alert(error.data.message);

            });
        }
    }.bind(this);

    this.setRegister = function () {
        this.isRegister = !this.isRegister;
    };

    $http.get('rest/enum/roles').success(function (roles) {
        $scope.roles = roles;
    })

}

app
    .service('InfoShareService', InfoShareService)
    .controller('LoginController', LoginController);

