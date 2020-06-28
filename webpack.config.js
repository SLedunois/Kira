const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
module.exports = {
  entry: "./portal/src/main/resources/index.tsx",
  target: "web",
  mode: "development",
  watchOptions: {
    poll: true
  },
  output: {
    path: path.resolve(__dirname, "portal", "src", "main", "resources", "dist"),
    filename: "bundle.js",
  },
  resolve: {
    extensions: [".js", ".jsx", ".json", ".ts", ".tsx"],
    alias: {
      "@assets": path.resolve(__dirname, 'portal', 'src', 'main', 'resources', 'assets'),
      "@ui": path.resolve(__dirname, 'portal', 'src', 'main', 'resources', 'components')
    }
  },
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        loader: "ts-loader",
      },
      {
        enforce: "pre",
        test: /\.js$/,
        loader: "source-map-loader",
      },
      {
        test: /\.css$/,
        use: [
          'style-loader',
          {loader: 'css-loader', options: {importLoaders: 1}},
          'postcss-loader'
        ]
      },
      {
        loader: require.resolve('file-loader'),
        exclude: [/\.(js|mjs|jsx|ts|tsx)$/, /\.html$/, /\.json$/, /\.css$/],
        options: {
          name: 'static/media/[name].[hash:8].[ext]',
        }
      }
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, "portal", "src", "main", "resources", "index.html"),
    })
  ],
};
