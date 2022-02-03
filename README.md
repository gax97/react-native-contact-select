# react-native-contact-select

react-native library for selecting contact from native android and iOS contacts picker.

## Installation

```sh
yarn add react-native-contact-select
```
or
```sh
npm install react-native-contact-select
```
Run pod install for iOS
```sh
pod install --project-directory=ios
```
## Permissions

### iOS
For iOS you don't need to handle permissions because native `CNContactPickerViewController` is being used.

### Android
For android add following to your `AndroidManifest.xml`:
```
<uses-permission android:name="android.permission.READ_CONTACTS" />
```
Permission will automatically be requested when you call for `selectContact` method.

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
