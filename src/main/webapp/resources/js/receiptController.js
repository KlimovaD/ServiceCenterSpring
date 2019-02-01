function ReceiptService($resource) {
    return $resource('rest/receipt?user=:user', {user: '@user'})
}

function ReceiptController($routeParams, $scope, $http, UserDevicesService, InfoShareService, UserReceiptsService, ReceiptService) {

    $scope.instance = InfoShareService.getUser();
    this.instance = $scope.instance;
    $scope.devices = UserDevicesService.query({login: $scope.instance.login})
    $scope.receipts = UserReceiptsService.query({login: $scope.instance.login, status:'all'});
    $scope.masterNotSet = false;
    $scope.invoiceExists = false;
    $scope.correctStatus = false;

    $http.get('rest/enum/receiptstatus').success(function (receipt_status) {
        $scope.receipt_status = receipt_status;
    });

    $http.get('rest/enum/type').success(function (types) {
        $scope.types = types;
    });

    this.create = function () {
        if(!$scope.serial)
            alert("Specify serial number of broken device");
        else if(!$scope.type)
            alert("Specify type of repair");
        else if(!$scope.malfunction)
            alert("Write down malfunction description");
        else{
            $http.get('rest/device/' + $scope.serial.split(':')[0])
                .then(function (value) {
                    var receipt = new ReceiptService();
                    receipt.device = value.data;
                    receipt.client = value.data.client;
                    receipt.receiver = $scope.instance;
                    receipt.malfunction = $scope.malfunction;
                    if($scope.note)
                        receipt.note = $scope.note;
                    receipt.repair_type = $scope.type;
                    receipt.status = $scope.status;
                    receipt.$save({user:$scope.instance.login}, function () {
                        $scope.malfunction = "";
                        $scope.note = "";
                        $scope.serial = "";
                        $scope.type = "";
                        $scope.status = "";

                        UserReceiptsService.query({login: $scope.instance.login}, function (value) {
                            $scope.receipts = value;
                        });
                    });
                });
        }
    };

    this.select = function (receipt) {
                device = receipt.device;
                $scope.status = receipt.status;
                if($scope.status === "Ready_for_extr")
                    $scope.correctStatus = true;
                else
                    $scope.correctStatus = false;
                $http.get("rest/invoice/receipt/" + receipt.id )
                    .then(function (value) {
                        if(value)
                            $scope.invoiceExists = true;
                        else
                            $scope.invoiceExists = false;
                    }, function (reason) {
                        $scope.invoiceExists = false;
                    });
                $scope.type = receipt.repair_type;
                $scope.malfunction = receipt.malfunction;
                if(receipt.note)
                    $scope.note = receipt.note;
                else
                    $scope.note = "";
                $scope.serial = device.serial + ': ' + device.brand + ' ' + device.model;
                if(!receipt.master)
                    $scope.masterNotSet = true;
                else
                    $scope.masterNotSet = false;
                $scope.selectedReceipt = receipt;
                InfoShareService.setReceipt(receipt);
    };

    this.setMaster = function () {
                $http.put('rest/receipt/' + $scope.selectedReceipt.id + '/set?master=' + $scope.instance.login)
                    .then(function (value2) {
                        UserReceiptsService.query({login: $scope.instance.login, status:'all'}, function (getedReceipt) {
                            $scope.receipts = getedReceipt;
                        });
                        alert('Successful update');
                        $scope.masterNotSet = false;
                    }, function (reason) {
                        alert(reason.data.message);
            });
    };

    this.updateReceipt = function () {
        updatedReceipt = $scope.selectedReceipt;

        updatedReceipt.status = $scope.status;
        updatedReceipt.repair_type = $scope.type;
        updatedReceipt.malfunction = $scope.malfunction;
        updatedReceipt.note = $scope.note;

        $http.put('rest/receipt/' + updatedReceipt.id + '?user=' + $scope.instance.login, updatedReceipt)
            .then(function (value) {
                UserReceiptsService.query({login: $scope.instance.login, status:'all'}, function (getedReceipt) {
                            $scope.receipts = getedReceipt;
                        });
                        alert('Successful update');
            }, function (reason) {
                alert(reason.data.message); })
    };

    this.createInvoice = function () {
        window.location.href = '#/user/' + this.instance.login + '/invoice';
    };
}

app
    .factory('ReceiptService', ReceiptService)
    .controller('ReceiptController', ReceiptController);
