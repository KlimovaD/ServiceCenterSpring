function UserService($resource) {
    return $resource('rest/user/:login', { login: '@login' });
}

function UserReceiptsService($resource) {
    return $resource('rest/user/:login/receipts?status=:status', { login: '@login', status: '@status' });
}

function UserInvoicesService($resource) {
    return $resource('rest/user/:login/invoices?status=:status', {login: '@login', status: '@status'});
}

function UserDevicesService($resource) {
    return $resource('rest/user/:login/devices', {login: '@login'});
}

function InfoController($scope, $http, $routeParams, UserService, UserReceiptsService, UserInvoicesService, UserDevicesService, InfoShareService) /*,
                                                    UserDevicesService) */{

    var url = function () {
        return {login:$routeParams.login};
    };

    var status_url = function (status) {
        return{login:$routeParams.login, status: status};

    };

    this.instance = InfoShareService.getUser();

    this.waitingInvoices = UserInvoicesService.query(status_url("waiting"));
    this.paidInvoices = UserInvoicesService.query(status_url("paid"));

    $scope.userDevices = UserDevicesService.query(url());

    this.openReceipts = function() {
        window.location.href = '#/user/'+ this.instance.login + '/receipts';
    };

    this.openDevices = function () {
        window.location.href = '#/user/'+ this.instance.login + '/devices';
    }
    
    this.checkButton = function () {
        if($scope.status === "all")
            $scope.receipts = UserReceiptsService.query(status_url("all"));
        else if ($scope.status === "opened")
            $scope.receipts = UserReceiptsService.query(status_url("opened"));
        else if ($scope.status === "in_progress")
            $scope.receipts = UserReceiptsService.query(status_url("in_progress"));
        else if ($scope.status === "closed")
            $scope.receipts = UserReceiptsService.query(status_url("closed"));
    }
}

app
    .factory('UserService', UserService)
    .factory('UserReceiptsService', UserReceiptsService)
    .factory('UserInvoicesService', UserInvoicesService)
    .factory('UserDevicesService', UserDevicesService)
    .controller('InfoController', InfoController);

