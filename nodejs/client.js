// const net = require('net');

// const socket = new net.Socket();

// const port = 8000;

// const hostname = '127.0.0.1';

// socket.setEncoding = 'UTF-8';

// socket.connect( port,hostname,function(){
//   socket.write('hello 大家好~~');
// });

// socket.on( 'data', function ( msg ) {
//   console.log( msg );
// });

// socket.on( 'error', function ( error ) {
//   console.log( 'error' + error );
// });

// socket.on('close',function(){
//   console.log('服务器端下线了');
// });


var net = require('net');
var port = 8085;
var host = '127.0.0.1';
var client= new net.Socket();
const cin = process.stdin;

//创建socket客户端
client.setEncoding('binary');
//连接到服务端
client.connect(port,host,function(){
  client.write('K: c = \\pm\\sqrt{a^2 + b^2}');
  //向端口写入数据到达服务端
});
cin.on('data',(chunk)=>{
    client.write(chunk);
})
client.on('data',function(data){
  console.log('from server:'+ data);
  //得到服务端返回来的数据
});
client.on('error',function(error){
//错误出现之后关闭连接
  console.log('error:'+error);
  client.destory();
});
client.on('close',function(){
//正常关闭连接
  console.log('Connection closed');
});