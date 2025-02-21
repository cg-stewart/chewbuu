import React from 'react';
import { FlatList, StyleSheet,  } from 'react-native';
import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";
import { Avatar } from "@/components/ui/avatar";
import { VStack } from "@/components/ui/vstack";
import { HStack } from "@/components/ui/hstack";
import { Divider } from "@/components/ui/divider";
import { Pressable } from "@/components/ui/pressable";

export function ChatsScreen() {
  return (
    <Box flex={1} bg="$backgroundDark950">
      {/* Video Messages Section */}
      <Box px={4} py={4}>
        <Text size="xl" bold>
          Video Messages
        </Text>
        <FlatList
          horizontal
          data={[]} // Video messages data
          renderItem={({ item }) => (
            <Pressable>
              <Box
                w={24}
                h={40}
                mr={2}
                bg="$backgroundDark800"
                rounded="lg"
                justifyContent="center"
                alignItems="center"
              />
            </Pressable>
          )}
        />
      </Box>

      {/* Active Chats */}
      <Box flex={1} px={4} py={4}>
        <Text size="xl" bold mb={4}>
          Active Chats
        </Text>
        <VStack space={4}>
          <FlatList
            data={[]} // Chat data
            renderItem={({ item }) => (
              <Pressable>
                <HStack space={4} p={4}>
                  <Avatar size="md" />
                  <VStack flex={1}>
                    <Text bold>
                      Chat Name
                    </Text>
                    <Text size="sm" color="$textLight400">
                      Last message...
                    </Text>
                  </VStack>
                </HStack>
              </Pressable>
            )}
          />
        </VStack>
      </Box>
    </Box>
  );
}

// Styles removed as we're using gluestack-ui styled components

