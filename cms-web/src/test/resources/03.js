var arguments = process.argv.splice(2);
console.log(arguments[0]);
const katex = require('katex');
var html = katex.renderToString(arguments[0], {
    throwOnError: false
});
console.log(html)