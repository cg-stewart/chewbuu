import React, { useState } from 'react';
import { FlatList, useWindowDimensions } from 'react-native';
import { TabView, SceneMap, TabBar } from 'react-native-tab-view';
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";
import { HStack } from "@/components/ui/hstack";
import { Button, ButtonText } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Pressable } from "@/components/ui/pressable";

const UpcomingRoute = () => (
  <FlatList
    data={[]} // Date data filtered by selected tab
    renderItem={({ item }) => (
      <Pressable>
        <Box style={{ padding: 16 }}>
          <Card style={{ backgroundColor: '#262626' }}>
            <Box style={{ padding: 16 }}>
              <Text style={{ color: '#ffffff' }}>Date details here...</Text>
            </Box>
          </Card>
        </Box>
      </Pressable>
    )}
  />
);

const ActiveRoute = () => (
  <Box style={{ padding: 16 }}>
    <Card style={{ backgroundColor: '#262626' }}>
      <Box style={{ padding: 16 }}>
        <Text style={{ fontSize: 20, color: '#ffffff', fontWeight: 'bold', marginBottom: 16 }}>
          Current Date
        </Text>
      </Box>
      <Box style={{ padding: 16 }}>
        <VStack space="lg">
          <Box style={{ backgroundColor: '#404040', padding: 16, borderRadius: 8 }}>
            <Text style={{ color: '#ffffff', fontWeight: '600', marginBottom: 8 }}>
              Safety Controls
            </Text>
            <HStack style={{ gap: 8 }}>
              <Button variant="outline" style={{ backgroundColor: '#dc2626' }}>
                Emergency
              </Button>
              <Button variant="outline">
                Record
              </Button>
            </HStack>
          </Box>
          <Box style={{ marginTop: 16 }}>
            <Text style={{ color: '#ffffff', fontWeight: '600', marginBottom: 8 }}>
              Add Photos & Videos
            </Text>
            <Button variant="outline">
              <ButtonText>Upload Media</ButtonText>
            </Button>
          </Box>
        </VStack>
      </CardBody>
    </Card>
  </Box>
);

const PastRoute = () => (
  <FlatList
    data={[]} // Past dates
    renderItem={({ item }) => (
      <Pressable>
        <Box style={{ padding: 16 }}>
          <Card style={{ backgroundColor: '#262626' }}>
            <Box style={{ padding: 16 }}>
              <Text style={{ color: '#ffffff' }}>Past date details...</Text>
            </Box>
          </Card>
        </Box>
      </Pressable>
    )}
  />
);

export function DatesScreen() {
  const layout = useWindowDimensions();
  const [index, setIndex] = useState(0);
  const [routes] = useState([
    { key: 'upcoming', title: 'Upcoming' },
    { key: 'active', title: 'Active' },
    { key: 'past', title: 'Past' },
  ]);

  return (
    <Box style={{ flex: 1, backgroundColor: "$backgroundDark950" }}>
      <TabView
        navigationState={{ index, routes }}
        renderScene={SceneMap({
          upcoming: UpcomingRoute,
          active: ActiveRoute,
          past: PastRoute,
        })}
        onIndexChange={setIndex}
        initialLayout={{ width: layout.width }}
        renderTabBar={props => (
          <TabBar
            {...props}
            style={{ backgroundColor: '#1a1a1a' }}
            indicatorStyle={{ backgroundColor: '#6366f1' }}
            labelStyle={{ color: '#ffffff' }}
          />
        )}
      />
    </Box>
  );
}

// Styles removed as we're using gluestack-ui styled components

