import { NativeModules, Platform } from 'react-native';
import type { IContact } from './types';

const LINKING_ERROR =
  `The package 'react-native-contact-select' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const ContactSelectModule = NativeModules.ContactSelect
  ? NativeModules.ContactSelect
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
export function selectContact(): Promise<IContact> {
  return ContactSelectModule.selectContact();
}
