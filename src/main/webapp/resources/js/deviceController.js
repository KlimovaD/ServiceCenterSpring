function DeviceService($resource) {
    return $resource('rest/device/:serial?user=:user', {serial: '@serial', user: '@user'});
}

function DeviceController($scope, $http, $routeParams, UserService, InfoShareService, UserDevicesService, DeviceService) {

    this.instance = InfoShareService.getUser();
    $scope.devices = UserDevicesService.query({login: this.instance.login});
    $scope.instance = this.instance;

    this.addDevice = function(){
        if(!$scope.serial) {
            alert('Enter serial number');
        } else if(!$scope.type){
            alert('Enter device type');
        } else if(!$scope.brand){
            alert('Enter device brand');
        } else if(!$scope.model) {
            alert('Enter device model');
        } else if(!$scope.client) {
            alert('Enter client login');
        } else {
            UserService.get({login:$scope.client}, function (value) {
                var device = new DeviceService();
                device.serial = $scope.serial;
                device.type = $scope.type;
                device.brand = $scope.brand;
                device.model = $scope.model;
                device.client = value;
                if($scope.purchase) {
                    device.purchase = $scope.purchase;
                }
                if($scope.warrantyexp) {
                    device.warrantyexp = $scope.warrantyexp;
                }
                if($scope.previous) {
                    device.previous = $scope.previous;
                }
                if($scope.warrantyexp) {
                    device.repairexp = $scope.repairexp;
                }
                device.$save({serial: $scope.serial, user: $scope.instance.login}, function () {
                    $scope.serial = "";
                    $scope.type = "";
                    $scope.brand = "";
                    $scope.model = "";
                    $scope.purchase = "";
                    $scope.warrantyexp = "";
                    $scope.previous = "";
                    $scope.repairexp = "";
                    UserDevicesService.query({login: $scope.instance.login}, function (value) {
                       $scope.devices = value;
                    });
                    alert('Device was created');
                }, function (error) {
                    alert(error.data.message);
                });
            });
        }
    }.bind(this);

    this.tableSelect = function(serial){
        $http.get('rest/device/' + serial).then(function (response) {
            device = response.data;
            $scope.serial = device.serial;
            $scope.type = device.type;
            $scope.brand = device.brand;
            $scope.model = device.model;
            $scope.client = device.client.login;
            if(device.purchase)
                $scope.purchase = new Date(device.purchase);
            else
                $scope.purchase = "";

            if(device.warrantyexp)
                $scope.warrantyexp = new Date(device.warrantyexp);
            else
                $scope.warrantyexp = "";

            if(device.previous)
                $scope.previous = new Date(device.previous);
            else
                $scope.previous = "";

            if(device.repairexp)
                $scope.repairexp = new Date(device.repairexp);
            else
                $scope.repairexp = "";

        }, function (reason) {
            alert(reason);
        });
    };

    this.updateDevice = function(){
        UserService.get({login: $scope.client}, function (client) {
            $scope.allclient = client;
            $http.get('rest/device/' + $scope.serial).then(function (value) {
                updatedDevice = value.data;

                updatedDevice.client = $scope.allclient;
                updatedDevice.purchase = $scope.purchase;
                updatedDevice.warrantyexp = $scope.warrantyexp;
                updatedDevice.previous = $scope.previous;
                updatedDevice.repairexp = $scope.repairexp;

                $http.put('rest/device/' + updatedDevice.serial + '?user=' + $scope.instance.login, updatedDevice)
                    .then(function (value2) {
                        UserDevicesService.query({login: $scope.instance.login}, function (getedDevice) {
                            $scope.devices = getedDevice;
                        });
                        alert('Successful update');
                    }, function (reason) {
                        alert(reason);
                    });
            }, function (reason) {
                alert(reason);
            });
        });
    }
}

app
    .factory('DeviceService', DeviceService)
    .controller('DeviceController', DeviceController);

