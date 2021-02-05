const merge             = require('webpack-merge');
const common            = require('./webpack.common.js');
const path              = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

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
            bypass: function(req, res, proxyOptions) {

                console.log(req.url);

                if (req.url.match(/\/clientlib\-site[a-z0-9\.]+\.css/)) return "/clientlib-site1/site1.css";
                else if (req.url.match(/\/clientlib\-site[a-z0-9\.]+\.js/)) return "/clientlib-site1/site1.js";
            },
            secure: false,
            changeOrigin: true
        }],
        
    }
});