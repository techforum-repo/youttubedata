const merge             = require('webpack-merge');
const common            = require('./webpack.common.js');
const path              = require('path');

module.exports = merge(common, {
    mode: 'development',
    devtool: 'inline-source-map',
    performance: { hints: 'warning' },

    devServer: {
        inline: true,
        index: '',
        proxy: [{
            context: () => true,
            target: 'http://localhost:4502',
             publicPath: "/dist/",
            bypass: function(req, res, proxyOptions) {

                console.log(req.url);

                if (req.url.match(/\/clientlib\-site1[a-z\.]+\.css/)) return "/clientlib-site1/site1.css";
                else if (req.url.match(/\/clientlib\-site1[a-z\.]+\.js/)) return "/clientlib-site1/site1.js";
                else if (req.url.match(/\/clientlib\-site2[a-z\.]+\.css/)) return "/clientlib-site2/site2.css";
                else if (req.url.match(/\/clientlib\-site2[a-z\.]+\.js/)) return "/clientlib-site2/site2.js"
            },
            secure: false,
            changeOrigin: true
        }],
        
    }
});