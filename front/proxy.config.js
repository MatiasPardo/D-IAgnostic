const proxyConfig = [
  {
    context: ['/api/*'],
    target: "http://localhost:8080",
    secure: false,
    changeOrigin: false ,
    logLevel : "debug"
  },
  {
    context: ['/apiv1'],
    target: "http://localhost:8080",
    secure: false,
    changeOrigin: false,
    logLevel : "debug"
  }

];

module.exports = proxyConfig;
