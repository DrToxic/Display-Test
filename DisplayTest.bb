If CommandLine$()="/debug" Then debug=True

gfxmodes = CountGfxModes()
xres	= GadgetWidth(Desktop())
yres	= GadgetHeight(Desktop())
w = 300 : h = 200 : d = 32
r = 0 : g = 0 : b = 0
xpos = 0 : ypos = 0
ttps	= 1
wind = CreateWindow("Display Tester",(xres/2)-175,(yres/2)-160,350,190,Desktop(),9)
SetStatusText wind,"Getting refresh rate..."
fps = RefreshRate()
timer = CreateTimer(ttps)
ftime = CreateTimer(fps)
	GraphicOn	= CreateButton ("Show Test Area",10,10,100,20,wind)
	GraphicOff	= CreateButton ("Hide Test Area",10,10,100,20,wind)
	FSTest		= CreateButton ("Full Screen Test",230,10,100,20,wind)
	DisableGadget FSTest
	HideGadget GraphicOff
	reslist		= CreateComboBox (117,10,107,20,wind)
		AddGadgetItem reslist,"Windowed"
		SelectGadgetItem reslist,0
	rslide		= CreateSlider (10,50,270,20,wind,1)
	gslide		= CreateSlider (10,80,270,20,wind,1)
	bslide		= CreateSlider (10,110,270,20,wind,1)
	SetSliderRange rslide,0,256
	SetSliderRange gslide,0,256
	SetSliderRange bslide,0,256
	rtext		= CreateTextField (285,50,50,20,wind)
	gtext		= CreateTextField (285,80,50,20,wind)
	btext		= CreateTextField (285,110,50,20,wind)
	DisableGadget rtext : SetGadgetText rtext,"0"
	DisableGadget btext : SetGadgetText btext,"0"
	DisableGadget gtext : SetGadgetText gtext,"0"
menu = WindowMenu(wind)
	info = CreateMenu("Information",0,menu)
		CreateMenu "Help",101,info
		CreateMenu "About",102,info
	act = CreateMenu ("Actions",0,menu)
		CreateMenu ("Redetermine refresh rate",201,act)
;		CreateMenu ("Prepare test sequence",202,act)
UpdateWindowMenu wind
For i=1 To gfxmodes
	AddGadgetItem reslist,GfxModeWidth(i)+" x "+GfxModeHeight(i)+" x "+GfxModeDepth(i)
Next
Repeat
SetStatusText wind,"Timer Speed: "+ttps+" Ticks Per Second.  Refresh Rate: "+fps+"FPS"
	SetGadgetText rtext,r
	SetGadgetText btext,b
	SetGadgetText gtext,g
;	SetSliderValue rslide,r
;	SetSliderValue gslide,g
;	SetSliderValue bslide,b
	Select WaitEvent()
		Case $101
			Select EventData()
				Case 1
					EndGraphics
					FS = False
					GEnabled = False
					HideGadget GraphicOFF
					ShowGadget GraphicON
					Case 15
						If dtext = False Then
							dtext = True
						Else
							dtext = False
						EndIf
				Case 19
					If random = False Then
						random = True
						flash = False
						FreeTimer(timer)
						timer = CreateTimer(ttps)
						DisableGadget rslide : DisableGadget bslide : DisableGadget gslide
					Else
					random = False
						EnableGadget rslide : EnableGadget bslide : EnableGadget gslide
					EndIf
					Case 20
						If ttps<fps Then ttps=ttps+1
						If timer<>0 Then FreeTimer(timer)
						timer = CreateTimer(ttps)
					Case 33
						If flash = False Then
							random = False
							flash = True
							FreeTimer(timer)
							timer = CreateTimer(ttps)
							DisableGadget rslide : DisableGadget bslide : DisableGadget gslide
						Else
							flash = False
							EnableGadget rslide : EnableGadget bslide : EnableGadget gslide
						EndIf
					Case 34
						If ttps>1 Then ttps=ttps-1
						If timer<>0 Then FreeTimer(timer)
						timer = CreateTimer(ttps)
					Case 47
						fade = True
						flash = False
						random = False
					Case 200 If ypos >0 Then ypos=ypos-1
					Case 203 If xpos >0 Then xpos=xpos-1
					Case 205 If xpos <4 Then xpos=xpos+1
					Case 208 If ypos <4 Then ypos=ypos+1
				Default
			End Select
		Case $103
				Select EventData()
					Case 81 If r<255 Then r=r+1
					Case 113 If r<255 Then r=r+1
					Case 65 If r>0 Then r=r-1
					Case 97 If r>0 Then r=r-1
					Case 87 If g<255 Then g=g+1
					Case 119 If g<255 Then g=g+1
					Case 83 If g>0 Then g=g-1
					Case 115 If g>0 Then g=g-1
					Case 69 If b<255 Then b=b+1
					Case 101 If b<255 Then b=b+1
					Case 68 If b>0 Then b=b-1
					Case 100 If b>0 Then b=b-1
					Default
				End Select
		Case $401
			Select EventSource()
				Case GraphicOn
					HideGadget GraphicOn
					ShowGadget GraphicOff
					Graphics w,h,d,2
					GEnabled = True
				Case GraphicOff
					HideGadget GraphicOff
					ShowGadget GraphicOn
					EndGraphics
					GEnabled = False
				Case FSTest
					EndGraphics
					Graphics w,h,d,1
					FS = True
					GEnabled = True
				Case rslide
					r=SliderValue(rslide)
					SetGadgetText rtext,SliderValue(rslide)
				Case gslide
					g=SliderValue(gslide)
					SetGadgetText gtext,SliderValue(gslide)
				Case bslide
					b=SliderValue(bslide)
					SetGadgetText btext,SliderValue(bslide)
				Case reslist
					If SelectedGadgetItem(reslist) > 0 Then
						EnableGadget FSTest
						w = GfxModeWidth(SelectedGadgetItem(reslist))
						h = GfxModeHeight(SelectedGadgetItem(reslist))
						d = GfxModeDepth(SelectedGadgetItem(reslist))
					Else
						DisableGadget FSTest
						w = 300 : h = 200 : d = 32
					EndIf
					If GEnabled = True Then
						If FS = True Then
							If SelectedGadgetItem(reslist)=0 Then
								EndGraphics
							ElseIf SelectedGadgetItem(reslist)>0 Then
								EndGraphics
								Graphics  w,h,d,1
							EndIf
						ElseIf FS = False Then
							EndGraphics
							Graphics w,h,d,2
						EndIf
					EndIf
				Default
			End Select
		Case $803
			EndGraphics
			Exit
		Case $1001
			Select EventData()
				Case 101 Notify "This application uses 12 keyboard buttons"+Chr$(13)+""+Chr$(13)+"ESC = exit fullscreen mode and hide the graphics area"+Chr$(13)+"TAB = Toggle colour values in graphics area"+Chr$(13)+"Q = increase RED value"+Chr$(13)+"W = increase GREEN value"+Chr$(13)+"E = increase BLUE value"+Chr$(13)+"A = decrease RED value"+Chr$(13)+"S = decrease GREEN value"+Chr$(13)+"D = decrease BLUE value"+Chr$(13)+"F = Cycle Colours (Black, Red, Green, Blue, White) in order"+Chr$(13)+"R = Random Colours"+Chr$(13)+"T = Increase Timer Ticks per Second"+Chr$(13)+"G = Decrease Timer Ticks Per Second"
				Case 102 Notify "This application was written by DrToxic, and is used to aid the detection of dead or stuck pixles on an LCD display."+Chr$(13)+"Compiled: 2017-10-13 at 3:57PM"+Chr$(13)+Chr$(13)+"Refresh-Rate function written by Geert Jan Alsem. Visit his site at http://geertjan.vze.com"
				Case 201 fps = RefreshRate()
			End Select
		Case $4001
			Select EventSource()
				Case timer
					If GEnabled = True Then
						If flash = True Then
							colour = colour + 1
							If colour >4 Then colour = 0
							Select colour
								Case 0 r=0 : g=0 : b=0
								Case 1 r=255 : g=0 : b=0
								Case 2 r=0 : g=255 : b=0
								Case 3 r=0 : g=0 : b=255
								Case 4 r=255 : g=255 : b=255
							End Select
						ElseIf random = True
							r = Rnd(0,255) : g = Rnd(0,255) : b = Rnd(0,255)
						EndIf
					EndIf
				Case ftime
					If fade = True
						For gi = g To 0
							For bi = b To 0
								For ri = r To 0
								Next
							Next
						Next
						fade = False
					EndIf
			End Select 
		Default
	End Select
If GEnabled = True Then
	Cls
	Color r,g,b
	Rect 0,0,w,h,True
	If dtext = True Then
		rr = ~r+256 : gg = ~g+256 : bb = ~b+256
		Color rr,gg,bb
		If debug=True Then
			Rect 0,0,250,34,True
		Else
			Rect 0,0,250,17,True
		EndIf
		Color r,g,b
		Text 0,0,"Red: "+r : Text  80,0,"Green: "+g : Text 176,0,"Blue: "+b
		If debug=True Then
			Text 0,17,"Red: "+rr : Text 80,17,"Green: "+gg : Text 176,17,"Blue: "+bb
		EndIf
	EndIf
	Flip
EndIf
Forever

Function RefreshRate()
	Include "Includes\Function RefreshRate().bb"
End Function