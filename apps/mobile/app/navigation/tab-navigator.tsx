import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Home, MessageSquare, PlusCircle, User, Calendar } from 'lucide-react-native';
import { HomeScreen } from '../screens/home-screen';
import { ChatsScreen } from '../screens/chats-screen';
import { NewDateScreen } from '../screens/new-date-screen';
import { ProfileScreen } from '../screens/profile-screen';
import { DatesScreen } from '../screens/dates-screen';

const Tab = createBottomTabNavigator();

export default function TabNavigator() {
  return (
    <Tab.Navigator
      screenOptions={{
        tabBarStyle: {
          backgroundColor: '#1a1a1a',
          borderTopColor: '#333',
        },
        tabBarActiveTintColor: '#fff',
        tabBarInactiveTintColor: '#666',
        headerStyle: {
          backgroundColor: '#1a1a1a',
        },
        headerTintColor: '#fff',
      }}
    >
      <Tab.Screen
        name="Home"
        component={HomeScreen}
        options={{
          tabBarIcon: ({ color }) => <Home color={color} />,
        }}
      />
      <Tab.Screen
        name="Chats"
        component={ChatsScreen}
        options={{
          tabBarIcon: ({ color }) => <MessageSquare color={color} />,
        }}
      />
      <Tab.Screen
        name="New Date"
        component={NewDateScreen}
        options={{
          tabBarIcon: ({ color }) => <PlusCircle color={color} size={32} />,
          tabBarLabel: '',
        }}
      />
      <Tab.Screen
        name="Dates"
        component={DatesScreen}
        options={{
          tabBarIcon: ({ color }) => <Calendar color={color} />,
        }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={{
          tabBarIcon: ({ color }) => <User color={color} />,
        }}
      />
    </Tab.Navigator>
  );
}
