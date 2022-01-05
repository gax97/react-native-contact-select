# react-native-contact-select

react-native library for selecting contact from native android and iOS contacts

## Installation

```sh
yarn add react-native-contact-select
```
```sh
npm install react-native-contact-select
```
Run pod install for iOS
```sh
pod install --project-directory=ios
```
## Permissions
At the moment this library doesn't handle permissions for you. Before running select contact make sure that the app has required permissions.

For iOS add this to your `Info.plist`:
```
<key>NSContactsUsageDescription</key>
<string>Explain why you use contacts</string>
```
For android add following to your `AndroidManifest.xml`:
```
<uses-permission android:name="android.permission.READ_CONTACTS" />
```
## Usage

```tsx
import { selectContact } from 'react-native-contact-select';

selectContact().then(contact => {
  // do something with contact
}).catch(error => {
  // handle error
})
```

## License

MIT



Project bootstrapped with [react-native-builder-bob](https://github.com/callstack/react-native-builder-bob)
