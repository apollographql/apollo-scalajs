var webpack = require('webpack');

module.exports = {
  "entry": "./opt-launcher.js",
  "output": {
    "path": __dirname,
    "filename": "[name]-bundle.js"
  },
  "module": {
    "preLoaders": [{
      "test": new RegExp("\\.js$"),
      "loader": "source-map-loader"
    }]
  },
  plugins: [
      new webpack.DefinePlugin({
        'process.env': {
          NODE_ENV: JSON.stringify('production')
        }
      }),
      new webpack.optimize.UglifyJsPlugin()
  ]
}