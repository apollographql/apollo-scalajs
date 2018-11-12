module.exports = {
  client: {
    service: {
      name: "test-schema",
      localSchemaFile: "tests/queries.json"
    },
    includes: [ "*.graphql" ]
  }
};
