const themeOptions = require('gatsby-theme-apollo-docs/theme-options');

module.exports = {
  pathPrefix: '/docs/scalajs',
  plugins: [
    {
      resolve: 'gatsby-theme-apollo-docs',
      options: {
        ...themeOptions,
        root: __dirname,
        subtitle: 'Apollo Scala.js',
        description: 'Use Apollo Client from your Scala.js applications',
        githubRepo: 'apollographql/apollo-scalajs',
        sidebarCategories: {
          null: [
            'index'
          ],
          Essentials: [
            'essentials/installation',
            'essentials/getting-started',
            'essentials/queries',
            'essentials/mutations'
          ],
          Advanced: [
            'advanced/fragments'
          ]
        }
      }
    }
  ]
};
