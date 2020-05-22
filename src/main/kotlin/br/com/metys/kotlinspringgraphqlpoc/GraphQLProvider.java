package br.com.metys.kotlinspringgraphqlpoc;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.visibility.NoIntrospectionGraphqlFieldVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {

        GraphQLSchema graphQLSchema = buildSchema();

        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        String sdlSchema = resourceToString("schema.graphqls");
        String sdlBook = resourceToString("BookType.graphqls");

        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        typeRegistry.merge(schemaParser.parse(sdlSchema));
        typeRegistry.merge(schemaParser.parse(sdlBook));

        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private String resourceToString(String nameURL) throws IOException {
        URL urlSchema = Resources.getResource(nameURL);
        return Resources.toString(urlSchema, Charsets.UTF_8);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                        .dataFetcher("personById", graphQLDataFetchers.getPersonByIdDataFetcher())
                        .dataFetcher("personByIdAndFirstName", graphQLDataFetchers.getPersonByIdAndFirstNameDataFetcher()))

                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .fieldVisibility(NoIntrospectionGraphqlFieldVisibility.NO_INTROSPECTION_FIELD_VISIBILITY)
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}
