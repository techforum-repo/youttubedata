const express = require('express');
const { ApolloServer } = require('apollo-server-express');
const { buildHTTPExecutor } = require('@graphql-tools/executor-http');
const { stitchSchemas } = require('@graphql-tools/stitch');
const { delegateToSchema } = require('@graphql-tools/delegate');
const { wrapSchema, schemaFromExecutor, RenameTypes, RenameRootFields } = require('@graphql-tools/wrap');
const { printSchema, buildSchema } = require('graphql');
const { makeExecutableSchema } = require('@graphql-tools/schema');
const axios = require('axios');

async function startServer() {
    const countriesRemoteExec = buildHTTPExecutor({ endpoint: 'https://countries.trevorblades.com/graphql' });
    const countriesLocalExec = buildHTTPExecutor({endpoint: 'http://localhost:4001/graphql'});
    const spaceXExec = buildHTTPExecutor({ endpoint: 'https://spacex-production.up.railway.app/' });

    const typeDefs = buildSchema(`
        type User {
            id: ID
            name: String
            username: String
            email: String
        }

        type Query {
            getUser(id: ID!): User
        }
  `);

    const userSchema = makeExecutableSchema({typeDefs});
    const countriesRemoteSubSchema = await schemaFromExecutor(countriesRemoteExec);
    const countriesLocalSubSchema = await schemaFromExecutor(countriesLocalExec);

    const transformedLocalCountrySchema = wrapSchema({
        schema: countriesLocalSubSchema,
        transforms: [
            new RenameTypes(name => (name === 'country' ? 'localCountry' : name)), 
            new RenameRootFields((operation, name) => (name === 'country' ? 'localCountry' : name)) 
        ],
    });

    const spaceXSubSchema = await schemaFromExecutor(spaceXExec);

    const gatewaySchema = stitchSchemas({
        subschemas: [
            {schema: userSchema},
            { schema: countriesRemoteSubSchema, executor: countriesRemoteExec },
            //{schema: countriesLocalSubSchema, executor: countriesLocalExec},
            {schema: transformedLocalCountrySchema, executor: countriesLocalExec},
            { schema: spaceXSubSchema, executor: spaceXExec },
        ],

        resolvers: {
            Query:
            {
                // Resolver for localCountry
                localCountry: async (parent, args, context, info) => {
                     try {
                         return delegateToSchema({
                             schema: {schema: countriesLocalSubSchema, executor: countriesLocalExec},
                             operation: 'query',
                             fieldName: 'country',
                             args,
                             context,
                             info,
                             
                         });
                     } catch (error) {
                         console.error('Error fetching localCountry:', error);
                         throw error;
                     }
                 },
                getUser: async (_, { id }) => {
                    console.log("id: "+id);
                    try {
                        const response = await axios.get(`http://localhost:3000/users/${id}`);
                        return response.data;  // Transform this as needed to match your GraphQL schema
                    } catch (error) {
                        console.error('Error fetching user:', error);
                        throw error;
                    }
                }
            }

        },



    });

    //console.log(printSchema(gatewaySchema));

    const server = new ApolloServer({
        schema: gatewaySchema,
    });

    await server.start();
    const app = express();
    server.applyMiddleware({ app });

    app.listen({ port: 4000 }, () => {
        console.log(`🚀 Server ready at http://localhost:4000/graphql`);
    });
}

startServer().catch(error => {
    console.error('Error starting server:', error);
});