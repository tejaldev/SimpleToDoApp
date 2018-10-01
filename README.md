# Pre-work - *SimpleToDoApp*

**SimpleToDoApp** is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Tejal Parulekar**

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/tejaldev/SimpleToDoApp/blob/master/ToDoList.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** HTML5 has been used widely for mobile web app. Advantage is one app works on all platforms and development skills doesn't need to platform specific. However layouts become too complicated when diffrent device needs to be supported. Android provides means to specify layouts for different devices. The layout designing is fairly easy using the IDE. 

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** An adapter maps the UI with its datasource. For instance in this app we use an ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items) which takes the list of items (i.e. datasource) and also the UI view template in which these items are to be shown. Speaking in terms of MVC pattern model, list of items-view is listView and controller is the Adapter. 

Technically all items in the listView have some view only difference is they will have different data. When the listview scrolls some items will go offscreen. The view of the item that went offscreen can still be reused. Instead of creating new view object when an item goes offscreen its view can be reused. ArrayAdapter's getView() method passes this view to be reused in convertView argument.

## Notes

Describe any challenges encountered while building the app.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
