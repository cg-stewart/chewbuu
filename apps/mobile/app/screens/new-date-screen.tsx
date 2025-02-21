import React, { useState } from 'react';
import { View, ScrollView, StyleSheet, Pressable } from 'react-native';
import { Coffee, Utensils, Gamepad2 } from 'lucide-react-native';
import { Button } from '@/components/ui/button';
import { Text } from '@/components/ui/text';

export function NewDateScreen() {
  const [step, setStep] = useState(1);
  const [selectedActivity, setSelectedActivity] = useState<string | null>(null);

  return (
    <ScrollView style={styles.container}>
      {/* Step 1: Activity Selection */}
      {step === 1 && (
        <View style={styles.step}>
          <Text style={styles.stepTitle}>What do you want to do?</Text>
          <View style={styles.activities}>
            {[
              { name: 'Eat', icon: Utensils },
              { name: 'Drink', icon: Coffee },
              { name: 'Play', icon: Gamepad2 },
            ].map((activity) => (
              <Pressable
                key={activity.name}
                style={[
                  styles.activityButton,
                  selectedActivity === activity.name && styles.selectedActivity,
                ]}
                onPress={() => setSelectedActivity(activity.name)}
              >
                <activity.icon
                  color={selectedActivity === activity.name ? '#fff' : '#666'}
                  size={24}
                />
                <Text
                  style={[
                    styles.activityText,
                    selectedActivity === activity.name && styles.selectedActivityText,
                  ]}
                >
                  {activity.name}
                </Text>
              </Pressable>
            ))}
          </View>
        </View>
      )}

      {/* Step 2: Venue Selection */}
      {step === 2 && (
        <View style={styles.step}>
          <Text style={styles.stepTitle}>Choose up to 3 places</Text>
          {/* Venue list will go here */}
        </View>
      )}

      {/* Step 3: Date & Time */}
      {step === 3 && (
        <View style={styles.step}>
          <Text style={styles.stepTitle}>When?</Text>
          {/* Date & Time picker will go here */}
        </View>
      )}

      {/* Step 4: Group Size */}
      {step === 4 && (
        <View style={styles.step}>
          <Text style={styles.stepTitle}>Who's joining?</Text>
          {/* Group size selector will go here */}
        </View>
      )}

      <View style={styles.navigation}>
        {step > 1 && (
          <Button
            variant="outline"
            onPress={() => setStep(step - 1)}
          >
            Back
          </Button>
        )}
        <Button
          variant="solid"
          onPress={() => {
            if (step < 4) setStep(step + 1);
            else {
              // Submit date request
            }
          }}
        >
          {step === 4 ? 'Find Matches' : 'Next'}
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
  step: {
    padding: 16,
  },
  stepTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 24,
  },
  activities: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  activityButton: {
    flex: 1,
    margin: 8,
    height: 100,
    backgroundColor: '#1a1a1a',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  selectedActivity: {
    backgroundColor: '#6366f1',
  },
  activityText: {
    color: '#666',
    marginTop: 8,
    fontSize: 16,
  },
  selectedActivityText: {
    color: '#fff',
  },
  navigation: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: 16,
    borderTopWidth: 1,
    borderTopColor: '#333',
  },
});
