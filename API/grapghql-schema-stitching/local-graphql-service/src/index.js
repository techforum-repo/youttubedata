const express = require("express");
const { buildSchema } = require("graphql");
const { makeExecutableSchema } =require('@graphql-tools/schema');
const { ApolloServer} = require('apollo-server-express');

async function startServer() {

			const schemaSDL = buildSchema(`
				type country {
					code: ID!
					name: String!
				}

				type Query {
					country(code: ID!): country
				}
				

		`);

		const resolvers = {
		  Query: {
			country: (parent, args, context, info) => {
			  console.log(args.code);

			  // Corrected response structure
			  const hardcodedCountry = {
				code: args.code,
				name: "Hardcoded Country1", // Replace with the actual data for the hardcoded code
			  };

			  // Return the hardcoded country
			  console.log(JSON.stringify(hardcodedCountry));
			  return hardcodedCountry;
			},
		  },
		};


		const schema = makeExecutableSchema({
		  typeDefs: schemaSDL,
		  resolvers,
		});

		   const server = new ApolloServer({
				schema: schema,
			 });

			await server.start();
			const app = express();
			server.applyMiddleware({ app });

			app.listen({ port: 4001 }, () => {
				console.log(`🚀 Server ready at http://localhost:4001/graphql`);
			});
	}


startServer().catch(error => {
    console.error('Error starting server:', error);
});


