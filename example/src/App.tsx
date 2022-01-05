import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { selectContact } from 'react-native-contact-select';
import type { IContact } from '../../src/types';

export default function App() {
  const [result, setResult] = React.useState<IContact | undefined>(undefined);

  console.log(result);
  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={() => selectContact().then(setResult)}>
        <Text>Select Contact</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
