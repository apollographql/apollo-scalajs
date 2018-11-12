module.exports = {
  client: {
    service: {
      name: "test-schema",
      localSchemaFile: "tests/mutations.json"
    },
    includes: [ "*.graphql" ]
  }
};
