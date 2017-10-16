*KEYWORD
*TITLE

$	Created with ANSYS Workbench v15.0
$
$	Units: mm, mg, ms, mN, K
$
*DATABASE_FORMAT
$   1IFORM  2IBINARY
         0
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                               NODE DEFINITIONS                               $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*NODE
$   1NID              2X              3Y              4Z     5TC     6RC
       1            1000               0               0
       2               0               0               0
       3            1000               0            1000
       4               0               0            1000
       5            1000            1000               0
       6               0            1000               0
       7            1000            1000            1000
       8               0            1000            1000
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                             ELEMENT DEFINITIONS                              $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*ELEMENT_SOLID
$   1EID    2PID      N1      N2      N3      N4      N5      N6      N7      N8
       1       1       1       2       4       3       5       6       8       7
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                             SECTION DEFINITIONS                              $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*SECTION_SOLID
$   1SECID   2ELFORM      3AET
         1         1          
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                             MATERIAL DEFINITIONS                             $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*MAT_ELASTIC
$     1MID       2RO        3E       4PR       5DA       6DB        7K
         1      7.85    2e+008       0.3                              
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                              PARTS DEFINITIONS                               $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*PART
$  HEADING
Solid
$     1PID    2SECID      3MID    4EOSID     5HGID     6GRAV   7ADPORT     8TMID
         1         1         1         0         0                              
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                               LOAD DEFINITIONS                               $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         1                                                            
$                 1A                  2O
                   0              1e+011
                 100              1e+011
                1000              1e+011
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         2                                                            
$                 1A                  2O
                   0        -5.5555e+006
                 100        -5.5555e+006
                1000        -5.5555e+006
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         3                                                            
$                 1A                  2O
                   0        8.33325e+006
                 100        8.33325e+006
                1000        8.33325e+006
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         4                                                            
$                 1A                  2O
                   0                   0
                 100         1.1111e+007
                1000         1.1111e+007
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         5                                                            
$                 1A                  2O
                   0                   0
                 100         1.1111e+007
                1000         1.1111e+007
*DEFINE_CURVE
$    1LCID     2SIDR      3SFA      4SFO     5OFFA     6OFFO   7DATTYP
         6                                                            
$                 1A                  2O
                   0                   0
                 100         1.1111e+007
                1000         1.1111e+007
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                                CONTROL OPTIONS                               $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*CONTROL_TERMINATION
$  1ENDTIM   2ENDCYC    3DTMIN   4ENDENG   5ENDMAS
       100  10000000      0.01        10         0
*CONTROL_TIMESTEP
$  1DTINIT   2TSSFAC     3ISDO   4TSLIMT    5DT2MS     6LCTM    7ERODE    8MS1ST
         0       0.9         0         0         0         1         1         0
*CONTROL_HOURGLASS
$     1IHQ       2QH
         1       0.1
*CONTROL_BULK_VISCOSITY
$      1Q1       2Q2     3TYPE
       1.5      0.06        -2
*CONTROL_SOLID
$   1ESORT   2FMATRX  3NIPTETS   4SWLOCL
         1   
*DAMPING_GLOBAL
$    1LCID   2VALDMP      3STX      4STY      5STZ      6SRX      7SRY      8SRZ
         0         0      
*CONTROL_ENERGY
$    1HGEN     2RWEN   3SLNTEN    4RYLEN
         2         1         2         2
*CONTROL_ACCURACY
$     1OSU      2INN   3PIDOSU
         1         4 
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                                 TIME HISTORY                                 $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*DATABASE_GLSTAT
$      1DT   2BINARY     3LCUR    4IOOPT    5DTHFF    6BINHF
        10
*DATABASE_MATSUM
$      1DT   2BINARY     3LCUR    4IOOPT    5DTHFF    6BINHF
        10
*DATABASE_NODOUT
$      1DT   2BINARY     3LCUR    4IOOPT    5DTHFF    6BINHF
        10
*DATABASE_ELOUT
$      1DT   2BINARY     3LCUR    4IOOPT    5DTHFF    6BINHF
        10
*DATABASE_BINARY_D3PLOT
$      1DT     2LCDT     3BEAM    4NPLTC
        10
*DATABASE_BINARY_RUNRSF
$      1DT       2NR
      5000
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                                  LIST SETS                                   $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*SET_NODE_LIST
$     1SID      2DA1      3DA2      4DA3      5DA4
         1                                        
$    1NID1     2NID2     3NID3     4NID4     5NID5     6NID6     7NID7     8NID8
         2         4         6         8
*SET_NODE_LIST
$     1SID      2DA1      3DA2      4DA3      5DA4
         2                                        
$    1NID1     2NID2     3NID3     4NID4     5NID5     6NID6     7NID7     8NID8
         1         3         5         7
*SET_NODE_LIST
$     1SID      2DA1      3DA2      4DA3      5DA4
         3                                        
$    1NID1     2NID2     3NID3     4NID4     5NID5     6NID6     7NID7     8NID8
         1         2         3         4         5         6         7         8
$
$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$                             BOUNDARY CONDITIONS                              $
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$
*LOAD_NODE_SET
$      1ID      2DOF     3LCID       4SF      5CID       6M1       7M2       8M3
         1         1         2     1.000         0                              
*LOAD_NODE_SET
$      1ID      2DOF     3LCID       4SF      5CID       6M1       7M2       8M3
         2         1         3     1.000         0                              
*BOUNDARY_PRESCRIBED_MOTION_SET_ID
$   1KeyID  2HEADING
         1Displacement
$      1ID      2DOF      3VAD     4LCID       5SF      6VID    7DEATH    8BIRTH
         3         1         2         4     1.000         0         0         0
*BOUNDARY_PRESCRIBED_MOTION_SET_ID
$   1KeyID  2HEADING
         2Displacement
$      1ID      2DOF      3VAD     4LCID       5SF      6VID    7DEATH    8BIRTH
         3         2         2         5     1.000         0         0         0
*BOUNDARY_PRESCRIBED_MOTION_SET_ID
$   1KeyID  2HEADING
         3Displacement
$      1ID      2DOF      3VAD     4LCID       5SF      6VID    7DEATH    8BIRTH
         3         3         2         6     1.000         0         0         0
*END
