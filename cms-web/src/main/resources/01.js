const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  await page.goto('http://127.0.0.1:5500/html/article/cc3d2502-dd9e-4c4d-b3f9-17dc50962c5b.html', {waitUntil: 'networkidle2'});
  await page.pdf({margin:{top:'20px'},displayHeaderFooter:true,printBackground:false,path: 'aa.pdf', format: 'A4',headerTemplate:'111'});

  await browser.close();
  console.log("success")
})();