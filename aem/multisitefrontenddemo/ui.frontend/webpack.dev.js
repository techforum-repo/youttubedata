const merge             = require('webpack-merge');
const common            = require('./webpack.common.js');
const path              = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src/main/webpack';

module.exports = env => {
    const writeToDisk = env && Boolean(env.writeToDisk);

    return merge(common, {
        mode: 'development',
        devtool: 'inline-source-map',
        performance: { hints: 'warning' },
        plugins: [
            
            new HtmlWebpackPlugin({
                inject: true,
                template: path.resolve(__dirname, SOURCE_ROOT + '/site1/static/index.html'),
                filename: 'site1.html',
                chunks: ['site1']
            }),
            new HtmlWebpackPlugin({
                inject: true,
                template: path.resolve(__dirname, SOURCE_ROOT + '/site2/static/index.html'),
                filename: 'site2.html',
                chunks: ['site2']
            })
        ],
        devServer: {
            inline: true,
            proxy: [{
                context: ['/content', '/etc.clientlibs'],
                target: 'http://localhost:4502',
            }],
            writeToDisk,
            liveReload: !writeToDisk
        }
    });
}