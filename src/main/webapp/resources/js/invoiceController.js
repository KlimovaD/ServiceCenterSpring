function  InvoiceService($resource) {
    return $resource('rest/invoice?user=:user', {user: '@user'})
}

function  InvoiceController($scope, $http, $routeParams, $InvoiceService, InfoShareService) {
    this.receipt = InfoShareService.getReceipt();
    $scope.instance = InfoShareService.getUser();

    this.createInvoice = function(){
        if(!$scope.price)
            alert('Please, specify the price');
        else{
            var invoice = new InvoiceService();
            invoice.receipt = $scope.receipt.id;
            invoice.price = $scope.price;
            invoice.client = $scope.receipt.client;
            invoice.receiver = $scope.receipt.receiver;
            invoice.$save({user: $scope.instance.login}, function () {
                alert('Invoice was successfuly created');
                $scope.price = "";
                window.location.href = '#/user/' + $scope.instance.login + '/receipts';
            })
        }
    }

}

app
    .factory('InvoiceService', InvoiceService)
    .controller('InvoiceController', InvoiceController);