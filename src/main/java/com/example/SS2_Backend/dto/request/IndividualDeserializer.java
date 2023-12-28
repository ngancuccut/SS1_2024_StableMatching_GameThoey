package com.example.SS2_Backend.dto.request;

import com.example.SS2_Backend.model.StableMatching.Individual;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

public class IndividualDeserializer extends StdDeserializer<Individual> {

	public IndividualDeserializer() {
		this(null);
	}

	public IndividualDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Individual deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		String individualName = node.get("individualName").asText();
		int individualSet = node.get("setType").asInt();
		int individualCapacity = node.get("capacity").asInt();

		Individual individual = new Individual();
		individual.setIndividualName(individualName);
		individual.setIndividualSet(individualSet);
		individual.setCapacity(individualCapacity);

		JsonNode propertiesNode = node.get("argument");

		if (propertiesNode != null && propertiesNode.isArray()) {
			Iterator<JsonNode> propertiesIterator = propertiesNode.elements();
			while (propertiesIterator.hasNext()) {
				JsonNode propertyNode = propertiesIterator.next();

				Double propertyValue = propertyNode.get(2).asDouble();
				int propertyWeight = propertyNode.get(1).asInt();
				String inputRequirement = propertyNode.get(0).asText();

				individual.setProperty(propertyValue, propertyWeight, inputRequirement);
			}
		}
		System.out.println("end custom deserializer");
		return individual;
	}

}
