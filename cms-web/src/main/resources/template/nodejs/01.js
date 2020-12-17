
const puppeteer = require('puppeteer');
const path = require('path');
async function getSvg(value) {
    const browser = await puppeteer.launch();
    try {

        const page = await browser.newPage();


        // await page.setViewport({ width, height });
        await page.goto(`file://${path.join(__dirname, 'index.html')}`);
        await page.evaluate(`document.body.style.background = 'white'`);
        /***
         * 输入文件
         */
        // const definition = fs.readFileSync(input, 'utf-8');
        // const definition = "graph TD\nA[Client] --> B[Load Balancer]";
        const definition = value;
        // console.log(value)

        await page.$eval('#container', function (container, definition) {
            container.innerHTML = definition;
            window.mermaid.init(undefined, container);
        }, definition);
        // await new Promise((resolve,reject)=>{
        //     let a = page.$eval('#container', function (container) {
        //         return container.innerHTML;
        //     })
        //     console.log(a)
        // });
        var svg = await page.$eval('#container', function (container) {
            return container.innerHTML;
        });
        // console.log(svg)
        // // resolve(svg)
        await browser.close();
        return svg;
    } catch (err) {
        console.log("语法错误!!")
        await browser.close();
    }
}
module.exports = getSvg
// getSvg("graph TD\nA[Client] --> B[Load Balancer]").then(v=>{
//     console.log(v)
// })
// console.log(b)