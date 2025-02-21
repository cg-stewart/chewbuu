import React from 'react';
import { View, ScrollView, StyleSheet } from 'react-native';
import { Text } from "@/components/ui/text";
import { Card} from "@/components/ui/card";
import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { HStack } from "@/components/ui/hstack";

export function HomeScreen() {
  return (
    <ScrollView style={styles.container}>
      {/* Featured Matches Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Featured Matches</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {/* Match cards will go here */}
        </ScrollView>
      </View>

      {/* Upcoming Dates Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Upcoming Dates</Text>
        <Card>
          {/* Upcoming date details */}
        </Card>
      </View>

      {/* Saved For Later Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Saved For Later</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {/* Saved matches will go here */}
        </ScrollView>
      </View>

      {/* Popular Venues Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Popular Venues</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {/* Venue cards will go here */}
        </ScrollView>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  section: {
    padding: 16,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 16,
  },
});
