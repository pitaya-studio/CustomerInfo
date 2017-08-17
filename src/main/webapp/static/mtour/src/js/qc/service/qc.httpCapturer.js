qc.config(['$httpProvider', function ($httpProvider) {
    var interceptorQc = ['qcMessage', '$q','urlConfig', function (qcMessage, $q,urlConfig) {
        return {
            'request': function (config) {
                return config;
            },
            'response': function (response) {
                if (response.data && response.data.responseType === 'mtour data') {
                    if (response.data.responseCode === 'success') {
                        //response.data = response.data;
                        return response;
                    }
                    else if (response.data.responseCode === 'fail') {
                        qcMessage.warning(response.data.msg.code+':'+response.data.msg.description);
                        return $q.reject(response);
                    }
                    else if (response.data.responseCode === 'authentication') {
                        qcMessage.warning('您还没有登录,请重新登录!')
                            .then(function () {
                                window.location=urlConfig.mtourLoginUrl;
                            });
                        return $q.reject(response);
                    }
                    else if(response.data.responseCode==='error'){
                        qcMessage.error('服务器发生异常,请联系管理员');
                        return $q.reject(response);
                    }else{
                        qcMessage.error('未定义的返回代码:'+response.data.responseCode);
                    }
                }
                else{
                    return response;
                }
            },
            'responseError': function (rejection) {
                if (rejection.responseType === 'mtour data') {
                    return $q.reject(rejection);
                }else{
                    qcMessage.error('发生未知异常,请联系管理员');
                    return $q.reject(rejection);
                }
            }
        };
    }];
    $httpProvider.interceptors.push(interceptorQc);
}]);