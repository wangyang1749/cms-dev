const katex = require('katex');
const mermaid = require("./01.js");

var html = katex.renderToString("c = \\pm\\sqrt{a^2 + b^2}");
var net = require('net');
//模块引入
var listenPort = 8085;//监听端口
var server = net.createServer(function (socket) {
  // 创建socket服务端
  console.log('connect: ' +
    socket.remoteAddress + ':' + socket.remotePort);
  socket.setEncoding('binary');
  //接收到数据
  socket.on('data', function (data) {
    console.log('client send:' + data);


    if (data.substr(0, 2) == "K:") {
      data = data.substr(2, data.length)
      try {

        let result = katex.renderToString(data)
        socket.write(result);
        socket.write("\n");
        socket.destroy()
        console.log("success");
      } catch (err) {
        //处理错误
        console.log("发生错误!!")
      }
    } else if (data.substr(0, 2) == "M:") {
      try {
        data = data.substr(2, data.length)
        var a = '';
        datas = data.split(",")
        for (var i = 0; i < datas.length; i++) {
          console.log(datas[i])
          a = a + datas[i] + "\n";
        }
        // "M:     graph TD,A[Client] --> B[Load Balancer]\n"
        mermaid(a).then(v => {
          socket.write(v);
          socket.write("\n");
          
          socket.destroy()
        })
      } catch (err) {
        console.log("服务器出错")
      }
      
    }
    
  });

  // socket.pipe(socket);
  //数据错误事件
  socket.on('error', function (exception) {
    console.log('socket error:' + exception);
    // socket.end();
  });
  //客户端关闭事件
  socket.on('close', function (data) {
    console.log('client closed!');
    // socket.remoteAddress + ' ' + socket.remotePort);
  });
}).listen(listenPort);
//服务器监听事件
server.on('listening', function () {
  console.log("server listening:" + server.address().port);
});
//服务器错误事件
server.on("error", function (exception) {
  console.log("server error:" + exception);
});