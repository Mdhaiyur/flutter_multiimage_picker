import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_multiimage_picker/flutter_multiimage_picker.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  List<Object?> pickedImages=[];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin - Multi Image picker'),
        ),
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                  onPressed: () async {
                    var images = await FlutterMultiimagePicker.openImagePicker(maxSelectable: 11);
                    setState(() {
                      pickedImages=images;
                    });
                  },
                  child: const Text('Open Image Picker')),
              Expanded(
                child: GridView.builder(
                    itemCount: pickedImages.length,
                    shrinkWrap: true,
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                    ),
                    itemBuilder: (BuildContext context, int index) {
                      return Card(
                        clipBehavior: Clip.antiAliasWithSaveLayer,
                        child: Image.file(File(pickedImages[index].toString()),fit: BoxFit.cover,),
                      );
                    }),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
