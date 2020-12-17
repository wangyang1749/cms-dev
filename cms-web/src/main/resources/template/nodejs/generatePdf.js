const puppeteer = require('puppeteer');
var arguments = process.argv.splice(2);


if (arguments.length < 2) {
    console.log('参数必须大于两个')
    return;
}
// 页脚
const footerTemplate = `<div 
        style="width:80%;margin:0 auto;font-size:8px; padding:10px 0;display: flex; justify-content: space-between; ">
        <a href="http://www.bioinfo.online/" style="color: rgba(0,0,0,.5);">http://www.bioinfo.online</a>
        <div><span class="pageNumber">
        </span> / <span class="totalPages"></span></div>
        </div>`;
const headerTemplate = `<div
        style="width:80%;margin:0 auto;font-size:8px;padding:10px 0;display: flex; justify-content: space-between;">
        <span></span>
        <span></span>
        </div>`;


// http://127.0.0.1:5500/html/article/cc3d2502-dd9e-4c4d-b3f9-17dc50962c5b.html

(async () => {
    const browser = await puppeteer.launch({ args: ['--no-sandbox', '--disable-setuid-sandbox'] });
    try {
        console.log("准备生成文件" + arguments[1])
        const page = await browser.newPage();
        await page.goto(arguments[0], { waitUntil: 'networkidle2' });
        // *{font-family: '楷体'} @page{margin: 27mm 16mm 27mm 16mm;} 
        await page.addStyleTag({ content: "body{font-size: 18px;} audio{ display:none} video{ display:none}  #header{display:none} @font-face{font:'abc';src: url('./abc.ttf')} " })
        await page.pdf({
            path: arguments[1],
            format: 'A4',
            footerTemplate,
            headerTemplate,
            displayHeaderFooter: true,
            margin: {
                top: 80,
                bottom: 80
            },
        });
        await browser.close();
        console.log("生成成功!!")
    } catch (err) {
        console.log('发生错误' + err);
        await browser.close();
    }

})();