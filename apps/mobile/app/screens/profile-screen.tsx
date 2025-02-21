import React from 'react';
import { View, ScrollView, StyleSheet, Pressable } from 'react-native';
import { Text } from "@/components/ui/text";
import { Avatar } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Box } from "@/components/ui/box";
import { VStack } from "@/components/ui/vstack";
import { HStack } from "@/components/ui/hstack";
import { Camera, Settings, Star, Clock, Ban } from 'lucide-react-native';

export function ProfileScreen() {
  return (
    <ScrollView style={styles.container}>
      {/* Profile Header */}
      <View style={styles.header}>
        <Avatar size="xl" />
        <Text style={styles.name}>User Name</Text>
        <Text style={styles.subscription}>SUGAR Tier</Text>
        
        <View style={styles.stats}>
          <View style={styles.stat}>
            <Text style={styles.statNumber}>4.8</Text>
            <Text style={styles.statLabel}>Rating</Text>
          </View>
          <View style={styles.stat}>
            <Text style={styles.statNumber}>23</Text>
            <Text style={styles.statLabel}>Dates</Text>
          </View>
          <View style={styles.stat}>
            <Text style={styles.statNumber}>12</Text>
            <Text style={styles.statLabel}>Friends</Text>
          </View>
        </View>
      </View>

      {/* Intro Video */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Intro Video</Text>
        <Pressable style={styles.videoContainer}>
          <Camera size={32} color="#fff" />
          <Text style={styles.videoText}>Update Intro Video</Text>
        </Pressable>
      </View>

      {/* Reviews */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Recent Reviews</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {/* Review cards will go here */}
        </ScrollView>
      </View>

      {/* Quick Actions */}
      <View style={styles.section}>
        <Button
          variant="outline"
          style={{ gap: 8 }}
          onPress={() => {}}
        >
          <Settings size={20} color="#fff" />
          Settings
        </Button>
        <Button
          variant="outline"
          style={{ gap: 8 }}
          onPress={() => {}}
        >
          <Star size={20} color="#fff" />
          Saved Matches
        </Button>
        <Button
          variant="outline"
          style={{ gap: 8 }}
          onPress={() => {}}
        >
          <Clock size={20} color="#fff" />
          Date History
        </Button>
        <Button
          variant="outline"
          style={{ gap: 8 }}
          onPress={() => {}}
        >
          <Ban size={20} color="#fff" />
          Blocked Users
        </Button>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  header: {
    alignItems: 'center',
    padding: 24,
    borderBottomWidth: 1,
    borderBottomColor: '#333',
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginTop: 16,
  },
  subscription: {
    fontSize: 16,
    color: '#6366f1',
    marginTop: 4,
  },
  stats: {
    flexDirection: 'row',
    marginTop: 24,
  },
  stat: {
    flex: 1,
    alignItems: 'center',
  },
  statNumber: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#fff',
  },
  statLabel: {
    fontSize: 14,
    color: '#666',
    marginTop: 4,
  },
  section: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#333',
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 16,
  },
  videoContainer: {
    height: 200,
    backgroundColor: '#1a1a1a',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  videoText: {
    color: '#fff',
    marginTop: 8,
    fontSize: 16,
  },
});
