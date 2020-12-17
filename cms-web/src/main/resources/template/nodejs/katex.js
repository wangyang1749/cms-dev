const katex = require('katex');
var arguments = process.argv.splice(2);
var html = katex.renderToString(arguments[0]);
console.log(html)