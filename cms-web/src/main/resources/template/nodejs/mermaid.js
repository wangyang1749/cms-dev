
const puppeteer = require('puppeteer');
const path = require('path');
var arguments = process.argv.splice(2);
(async ()=> {
    const browser = await puppeteer.launch({ args: ['--no-sandbox', '--disable-setuid-sandbox'] });
    try {
        const page = await browser.newPage();

        await page.goto(`file://${path.join(__dirname, 'index.html')}`);
        await page.evaluate(`document.body.style.background = 'white'`);
     
        var result = '';
        datas = arguments[0].split("\\n")
        for (var i = 0; i < datas.length; i++) {
            // console.log(datas[i])
          result = result + datas[i] + "\n";
        }
        // const definition = "graph TD\nA[Client] --> B[Load Balancer] ";
        const definition = result;
        // console.log(result)
        await page.$eval('#container', function (container, definition) {
            container.innerHTML = definition;
            window.mermaid.init(undefined, container);
        }, definition);
        var svg = await page.$eval('#container', function (container) {
            return container.innerHTML;
        });
        console.log(svg)

        await browser.close();
    } catch (err) {
        console.log("语法错误!!")
        await browser.close();
    }
})()


