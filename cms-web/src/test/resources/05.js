 function getHtml() {
            var result = ""
            var graphDefinition = 'graph TB\na-->b';
            var graph = mermaid.mermaidAPI.render('graphDiv', graphDefinition, (svg) => {
                result = svg
                console.log(svg)

            });
            return result;
        }