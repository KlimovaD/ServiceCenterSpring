var app = angular.module('app', ['ngRoute','ngResource']);
app.config(['$routeProvider', function($routeProvider){
    $routeProvider
        .when('/', {
            redirectTo: '/login'
        })
        .when('/login', {
            templateUrl: 'view/loginPage.html',
            controller: 'LoginController',
            controllerAs: 'loginCtrl'
        })
        .when('/user/:login', {
            templateUrl: 'view/infoPage.html',
            controller: 'InfoController',
            controllerAs: 'infoCtrl'
        })
        .when('/user/:login/receipts', {
            templateUrl: 'view/receiptPage.html',
            controller: 'ReceiptController',
            controllerAs: 'receiptCtrl'
        })
        .when('/user/:login/devices', {
            templateUrl: 'view/devicePage.html',
            controller: 'DeviceController',
            controllerAs: 'deviceCtrl'
        })
        .when('/user/:login/invoice', {
            templateUrl: 'view/invoicePage.html',
            controller: 'InvoiceController',
            controllerAs: 'invoiceCtrl'
        })
        .otherwise(
            { redirectTo: '/'}
        );
}]);
