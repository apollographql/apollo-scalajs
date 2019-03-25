module.exports = {
  __experimentalThemes: [
    {
      resolve: 'gatsby-theme-apollo-docs',
      options: {
        root: __dirname,
        subtitle: 'Apollo Scala.js',
        description: 'Use Apollo Client from your Scala.js applications',
        contentDir: 'docs/source',
        basePath: '/docs/scalajs',
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
